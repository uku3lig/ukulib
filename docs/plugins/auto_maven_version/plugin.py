from mkdocs.config import base, config_options as c
from mkdocs.plugins import BasePlugin
from mkdocs.structure.files import Files

import urllib
import re
import logging

log = logging.getLogger(f"mkdocs.plugins.{__name__}")


class AutoMavenVersionPluginConfig(base.Config):
    metadata_url = c.Type(str)


class AutoMavenVersionPlugin(BasePlugin[AutoMavenVersionPluginConfig]):
    def on_files(self, files: Files, config):
        res = urllib.request.urlopen(self.config.metadata_url)
        match = re.search("<latest>(.+)</latest>", res.read().decode("utf-8"))
        latest_ver = match.group(1)
        short_ver = latest_ver.split("+")[0]
        log.debug(f"Found latest version: {latest_ver} (short: {short_ver})")

        for file in files:
            if file.is_documentation_page() and "%MAVEN" in file.content_string:
                file.content_string = file.content_string.replace(
                    "%MAVEN_VERSION%", latest_ver
                ).replace("%MAVEN_SHORT%", short_ver)
