# How Braid Bots work

The current state of Braid bots is deliberately simple.
We anticipate extending the API available to bots as we start to write more and see how they are used.

## Example Bots

The easiest way to get started writing a bot is with the [braidbot library](https://github.com/braidchat/braidbot) (which is in [Racket](https://racket-lang.org)).

For a bot written using the above library, see [rot13bot](https://github.com/braidchat/rot13bot) or [reminderbot](https://github.com/braidchat/reminderbot).

If you'd like to write a bot from scratch or in a another language, you may find the below instructive:

| Bot                                                   | Language | Notes                                                                                    |
|-------------------------------------------------------|----------|------------------------------------------------------------------------------------------|
| [giphybot](https://github.com/braidchat/giphybot)     | Rust     | Simple bot, finds a gif using the giphy API based on the message sent to it              |
| [octocat](https://github.com/braidchat/octocat)       | Rust     | More complicated bot, allows interacting with pre-set GitHub repositories                |
| [emailbot](https://github.com/braidchat/emailbot)     | Elixir   | Uses OAuth to connect to Gmail inboxes, manage inbox, compose emails with a Braid thread |
| [greeterbot](https://github.com/braidchat/greeterbot) | Haskell  | Monitors all events in a group (not just mentions) to welcome new users to the group     |

## Creating bots

Creating a bot can be done through the web interface, or in the repl as follows:

    (db/create-bot!
      {:id (db/uuid)
       :name "giphybot"
       :avatar "https://s3.amazonaws.com/chat.leanpixel.com/uploads/5730f31a-8b10-451d-a1b0-3c515045481c/ptero.gif"
       :webhook-url "http://localhost:10000/message"
       :group-id some-group-id})

The created bot will have `:token` and `:user-id` fields added to it.
The token is used along with the id of the bot to authenticate requests it makes (as described below).
The user-id is a "fake" user that the bot's messages will be created under.
The fake user-id can probably be ignored by your bot, as any messages created by your bot will automatically be given the appropriate user-id.

## Receiving

Bots are sent any messages in their group that begin with a forward-slash (`/`) and their name (e.g. `/giphybot`).
The messages are sent as [MessagePack-encoded Transit][transit] via a `PUT` request to the webhook-url specified for the bot.
The `PUT` request to the bot includes a header `X-Braid-Signature` whose value is the HMAC-SHA256 of the request body, with the key being the bot token.
The server ignores any response from the bot.

Messages have the following schema:

    {:id Uuid
     :thread-id Uuid
     :group-id Uuid
     :user-id Uuid
     :content Str
     :created-at Inst
     :mentioned-user-ids [Uuid]
     :mentioned-tag-ids [Uuid]}

## Sending

Requests must be authenticated with HTTP Basic auth, where the username is the (stringifed) bot id (e.g. `"575b4e3b-a951-4d87-8c1c-6153f8402d2c"`) and the password is the bot's token.

### Creating Messages

To create a message, the bot can send a `POST` request to `/bots/message` endpoint of the api server (e.g. `https://api.braid.chat`).
The message sent must be in the same format as the server sends --- MessagePack-encoded Transit, with the same schema as shown above.
However, the `user-id`, `group-id`, and `created-at` fields can be omitted as the server will fill them in with the bot's faux user-id, the group of the bot, and the current server time, respectively.
The server will return the following error codes:

  - 201 if the message is successfully created
  - 400 if the message is malformed (either invalid transit data or not conforming to the message schema)
  - 401 if HTTP Basic auth fails
  - 403 if the bot tries to create a message it isn't allowed to (i.e. in a different group, with users or tags not in the group)

### Subscribing

A bot can subscribe to a thread by sending a `PUT` request to `/bots/subscribe/<thread-id>`, authenticated in the same way as sending.
When subscribed to a thread, whenever a user replies to that thread, the bot will receive the message in the same manner described above under "Receiving".
The server will return the following error codes:

  - 201 if the subscription is successful
  - 400 if the thread-id is malformed
  - 401 if HTTP Basic auth fails
  - 403 if the thread is in a different group from the bot

### Group Events

When creating a bot, if you give it an event webhook url, it will also receive all messages broadcast to the group.
Events are vectors where the first element is a keyword indicating the event name and the second element is the event payload.

The events sent are currently:

```
:braid.client/thread
:braid.client/init-data
:socket/connected
:braid.client/create-tag
:braid.client/joined-group
:braid.client/update-users
:braid.client/invitation-received
:braid.client/name-change
:braid.client/user-new-avatar
:braid.client/left-group
:braid.client/user-connected
:braid.client/user-disconnected
:braid.client/new-user
:braid.client/user-left
:braid.client/new-admin
:braid.client/tag-descrption-change
:braid.client/retract-tag
:braid.client/new-intro
:braid.client/group-new-avatar
:braid.client/publicity-changed
:braid.client/new-bot
:braid.client/retract-bot
:braid.client/edit-bot
:braid.client/notify-message
:braid.client/hide-thread
:braid.client/show-thread
```

### All Messages

When creating a bot, you can also check the "Receive all public messages" box, in which case the bot will be sent all messages in the group that are in a thread with at least one tag.

### Other Routes

The bot can send a `GET` request to `/bots/names/<user-id>` to get the nickname for a user with the given id.
The server will return the following error codes:

  - 200 if successful, with the nickname as the plain text body
  - 400 if the user-id is malformed
  - 401 if HTTP Basic auth fails
  - 403 if the user is one the bot isn't allowed to see (e.g. not in the bot's group)

  [transit]: https://github.com/cognitect/transit-format
