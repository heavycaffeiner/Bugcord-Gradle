#!/usr/bin/env bash
# Downloads the prebuilt artifacts this build resolves, into local-repo/.
set -euo pipefail

HOST="${BUGCORD_ARTIFACT_HOST:-https://github.com/thirdscam}"
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

download() {
    local url="$1" path="$2"
    mkdir -p "$ROOT/local-repo/$(dirname "$path")"
    curl -fsSL --retry 5 --retry-delay 2 -o "$ROOT/local-repo/$path" "$url"
}

download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-base-cmd-2.4.0.pom" "com/aliucord/d2j/d2j-base-cmd/2.4.0/d2j-base-cmd-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-base-cmd-2.4.0.jar" "com/aliucord/d2j/d2j-base-cmd/2.4.0/d2j-base-cmd-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-base-cmd-2.4.0.module" "com/aliucord/d2j/d2j-base-cmd/2.4.0/d2j-base-cmd-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-external-2.4.0.pom" "com/aliucord/d2j/d2j-external/2.4.0/d2j-external-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-external-2.4.0.jar" "com/aliucord/d2j/d2j-external/2.4.0/d2j-external-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-jasmin-2.4.0.pom" "com/aliucord/d2j/d2j-jasmin/2.4.0/d2j-jasmin-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-jasmin-2.4.0.jar" "com/aliucord/d2j/d2j-jasmin/2.4.0/d2j-jasmin-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-jasmin-2.4.0.module" "com/aliucord/d2j/d2j-jasmin/2.4.0/d2j-jasmin-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-smali-2.4.0.pom" "com/aliucord/d2j/d2j-smali/2.4.0/d2j-smali-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-smali-2.4.0.jar" "com/aliucord/d2j/d2j-smali/2.4.0/d2j-smali-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/d2j-smali-2.4.0.module" "com/aliucord/d2j/d2j-smali/2.4.0/d2j-smali-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-ir-2.4.0.pom" "com/aliucord/d2j/dex-ir/2.4.0/dex-ir-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-ir-2.4.0.jar" "com/aliucord/d2j/dex-ir/2.4.0/dex-ir-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-ir-2.4.0.module" "com/aliucord/d2j/dex-ir/2.4.0/dex-ir-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-reader-api-2.4.0.pom" "com/aliucord/d2j/dex-reader-api/2.4.0/dex-reader-api-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-reader-api-2.4.0.jar" "com/aliucord/d2j/dex-reader-api/2.4.0/dex-reader-api-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-reader-api-2.4.0.module" "com/aliucord/d2j/dex-reader-api/2.4.0/dex-reader-api-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-reader-2.4.0.pom" "com/aliucord/d2j/dex-reader/2.4.0/dex-reader-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-reader-2.4.0.jar" "com/aliucord/d2j/dex-reader/2.4.0/dex-reader-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-reader-2.4.0.module" "com/aliucord/d2j/dex-reader/2.4.0/dex-reader-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-tools-2.4.0.pom" "com/aliucord/d2j/dex-tools/2.4.0/dex-tools-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-tools-2.4.0.jar" "com/aliucord/d2j/dex-tools/2.4.0/dex-tools-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-tools-2.4.0.module" "com/aliucord/d2j/dex-tools/2.4.0/dex-tools-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-translator-2.4.0.pom" "com/aliucord/d2j/dex-translator/2.4.0/dex-translator-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-translator-2.4.0.jar" "com/aliucord/d2j/dex-translator/2.4.0/dex-translator-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-translator-2.4.0.module" "com/aliucord/d2j/dex-translator/2.4.0/dex-translator-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-writer-2.4.0.pom" "com/aliucord/d2j/dex-writer/2.4.0/dex-writer-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-writer-2.4.0.jar" "com/aliucord/d2j/dex-writer/2.4.0/dex-writer-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex-writer-2.4.0.module" "com/aliucord/d2j/dex-writer/2.4.0/dex-writer-2.4.0.module"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex2jar-2.4.0.pom" "com/aliucord/d2j/dex2jar/2.4.0/dex2jar-2.4.0.pom"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex2jar-2.4.0.jar" "com/aliucord/d2j/dex2jar/2.4.0/dex2jar-2.4.0.jar"
download "$HOST/Bugcord-Maven/releases/download/2.4.0/dex2jar-2.4.0.module" "com/aliucord/d2j/dex2jar/2.4.0/dex2jar-2.4.0.module"

echo "Fetched the build dependencies into $ROOT/local-repo"
