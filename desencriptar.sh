#!/bin/bash
ls -la
echo $ENCRYPTION_PASSWORD | gpg --passphrase-fd 0 -d ./script_inicializacion.tar.gpg | tar xvf -
