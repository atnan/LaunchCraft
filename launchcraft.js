var LaunchCraft = {
    launch: function(username, password, server) {
      return LaunchCraft.applet.jsLaunchMinecraft(username, password, server);
    },

    isMinecraftDownloaded: function() {
      return (LaunchCraft.applet.jsIsMinecraftDownloaded() == "true");
    },

    onAppletDownloadMinecraftSuccess: function() {
      if (typeof(LaunchCraft.onDownloadMinecraftSuccess) == typeof(Function)) {
        LaunchCraft.onDownloadMinecraftSuccess();
      }
    },
    onAppletDownloadMinecraftFail: function() {
      if (typeof(LaunchCraft.onDownloadMinecraftFail) == typeof(Function)) {
        LaunchCraft.onDownloadMinecraftFail();
      }
    },
    onDownloadMinecraftSuccess: null,
    onDownloadMinecraftFail: null,
    downloadMinecraft: function(successCallback, failCallback) {
      LaunchCraft.onDownloadMinecraftSuccess = successCallback;
      LaunchCraft.onDownloadMinecraftFail = failCallback;

      LaunchCraft.applet.jsDownloadMinecraft();
    },

    onLoad: null,
    load: function(onLoadCallback) {
        if (!document.getElementById('LaunchCraft') && !LaunchCraft.applet) {
            if (typeof(onLoadCallback) == typeof(Function)) {
              LaunchCraft.onLoad = onLoadCallback;
            }

            var embed = document.createElement('embed');

            embed.setAttribute('id', 'LaunchCraft');
            embed.setAttribute('type', 'application/x-java-applet');
            embed.setAttribute('archive', 'LaunchCraft.jar');
            embed.setAttribute('code', 'LaunchCraft.class');
            embed.setAttribute('mayscript', 'true');

            embed.setAttribute('style', 'width: 0; height: 0;');
            embed.setAttribute('width', '0');
            embed.setAttribute('height', '0');

            var launchCraftElement = null;
            try {
              document.body.appendChild(embed);
              launchCraftElement = document.getElementById("LaunchCraft");

            } catch(err) {
              return false;
            }

            if (launchCraftElement == null) {
              return false;
            }
        }
        return true;
    },
    applet: null,
    appletLoaded: false,
    hasAppletLoaded: function() {
        return LaunchCraft.appletLoaded;
    },
    onAppletLoaded: function(applet) {
        LaunchCraft.applet = applet;
        LaunchCraft.appletLoaded = true;

        if (typeof(LaunchCraft.onLoad) == typeof(Function)) {
          LaunchCraft.onLoad();
        }
    }
};

var LaunchCraft_onAppletLoaded = LaunchCraft.onAppletLoaded;
var LaunchCraft_onAppletDownloadMinecraftSuccess = LaunchCraft.onAppletDownloadMinecraftSuccess;
var LaunchCraft_onAppletDownloadMinecraftFail = LaunchCraft.onAppletDownloadMinecraftFail;
