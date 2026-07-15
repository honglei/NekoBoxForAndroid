#!/bin/bash

set -e

DIR=app/src/main/assets/sing-box
rm -rf $DIR
mkdir -p $DIR
cd $DIR

get_latest_release() {
  local release_url version
  release_url=$(curl --fail --location --silent --show-error \
    --retry 3 --retry-all-errors --output /dev/null --write-out '%{url_effective}' \
    "https://github.com/$1/releases/latest")
  version=${release_url##*/}
  if [[ -z $version || $version == latest ]]; then
    echo "Unable to resolve the latest release for $1" >&2
    return 1
  fi
  printf '%s\n' "$version"
}

####
VERSION_GEOIP=$(get_latest_release "SagerNet/sing-geoip")
echo VERSION_GEOIP=$VERSION_GEOIP
echo -n $VERSION_GEOIP > geoip.version.txt
curl -fLSsO https://github.com/SagerNet/sing-geoip/releases/download/$VERSION_GEOIP/geoip.db
xz -9 geoip.db

####
VERSION_GEOSITE=$(get_latest_release "SagerNet/sing-geosite")
echo VERSION_GEOSITE=$VERSION_GEOSITE
echo -n $VERSION_GEOSITE > geosite.version.txt
curl -fLSsO https://github.com/SagerNet/sing-geosite/releases/download/$VERSION_GEOSITE/geosite.db
xz -9 geosite.db
