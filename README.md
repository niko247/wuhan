# Wuhan Poland
Coronavirus check for Poland.
Checks for new cases of coronavirus infections in Poland and send push notifications.

Requires Java>=19

In order to send send push notification via Telegram given environment variables must be set:

**TELEGRAM_TOKEN=XXXXXXXXXXX**

and changed chat_id in code.

All sensitive environment variables should be in new environment file **.env**

Application can be deployed to Heroku using included Procfile
