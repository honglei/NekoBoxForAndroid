#!/bin/bash

set -euo pipefail

buildScript/lib/core/init.sh
pushd ../sing-box
go test ./boxapi ./nekoutils ./common/dialer ./protocol/group ./protocol/vless ./route/rule
popd
buildScript/lib/core/build.sh
