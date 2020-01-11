# Sky Gaze Bot

Sky Gaze is a weather prediction Facebook messenger chatbot which serves your weather requests by location.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
Make sure the documentation to set up third party endpoints are accurate as of the day you clone this repository

### Prerequisites

Make sure you install these softwares before cloning the repository:
* [Eclipse](https://www.eclipse.org/downloads/) - IDE for development
* [Tomcat](https://tomcat.apache.org/download-80.cgi#8.5.14) - The web server used
* [Git](https://git-scm.com/downloads) - For distributed development
* [Ngrok](https://ngrok.com) - Required for exposing a public URL for your localhost

### Installing

Start with these steps only once the pre-requisites are satisfied:

* Step 1 - Clone sky-gaze-bot repository

```
Clone the repository using following command in your git-bash, terminal:
git clone git@github.com:code-shaan/sky-gaze-bot.git
```

* Step 2 - Sign up for a free API.AI account

```
This is where your bot agent will reside:
URL: https://api.ai
```

* Step 3 - Upload API.AI agent

```
Login to your API.AI account
Click "Create New Agent"
Provide all required information
Click the gear icon for settings
Click "Export and Import" tab
Click Import button to upload zip file "Agent-SkyGaze.zip" provided in the project
```

* Step 4 - Starting local servers

```
In Eclipse, add and start your local Tomcat server:
https://crunchify.com/step-by-step-guide-to-setup-and-install-apache-tomcat-server-in-eclipse-development-environment-ide/

In Terminal, start your Ngrok server
./ngrok http 8080
```

* Step 5 - Facebook Integration

```
This is the final step in your bot setup.
After the completion of this step, you should be able to test your Facebook chatbot end-to-end

Follow the doc steps provided here to integrate your API.AI agent with Facebook:
https://docs.api.ai/docs/facebook-integration
```



## Author

* **Shantanu S** [GitHub](https://github.com/code-shaan)

## License

This project is licensed under GNU GENERAL PUBLIC LICENSE v3.0 - see the [COPYING](COPYING) file for details
