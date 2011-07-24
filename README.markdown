Downloads the latest Minecraft client & connects to a Minecraft server:

    LaunchCraft.load(function() {
      LaunchCraft.downloadMinecraft(function() {
        LaunchCraft.launch("username", "password", "example.com");
      });
    });

LaunchCraft.js uses a signed Java applet to achieve the elevated privileges it needs to download & run Minecraft. To generate a public/private key pair and self-signed certificate, use the following command:

    $ keytool -genkey -alias signFiles -keystore keystore.db

You can then build & sign LaunchCraft.jar:

    $ make sign
