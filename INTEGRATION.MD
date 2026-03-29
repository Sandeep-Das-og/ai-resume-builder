# Integrations

## Brevo (Email)

### Configuration
The backend reads these environment variables:
- `BREVO_API_KEY` (required)
- `BREVO_SENDER_EMAIL` (recommended)
- `BREVO_SENDER_NAME` (optional)

Defaults (from `application.yml`):
- `BREVO_SENDER_EMAIL`: `noreply@example.com`
- `BREVO_SENDER_NAME`: `AI Resume Builder`
- `maxContentChars`: `20000`

### How to get the API key
1. Create a free Brevo account.
2. Go to **SMTP & API** settings.
3. Create a new **v3 API key**.
4. Copy and store it as `BREVO_API_KEY`.

### Sender verification (required)
Brevo requires a verified sender email or domain. Add and verify your sender in Brevo before sending emails, otherwise requests will fail.

### Local usage
Example shell env:
```
export BREVO_API_KEY=your_key_here
export BREVO_SENDER_EMAIL=verified-sender@yourdomain.com
export BREVO_SENDER_NAME="AI Resume Builder"
```

### Render deployment
Add the same variables in your Render service **Environment Variables** section:
- `BREVO_API_KEY`
- `BREVO_SENDER_EMAIL`
- `BREVO_SENDER_NAME`
