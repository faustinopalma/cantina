@echo off

REM Need to change path so that dynamically linked native libraries can
REM be loaded.

set _PATH_ORIG=%PATH%

set PATH=.\bin;%PATH%

java -cp bin/one.world.jar;bin/one.tools.jar;bin/one.apps.jar;bin/db.jar;bin/BCEL.jar -Done.world.config.name=./config/one.world.config -Done.world.home=. -Done.world.store.root=./tuplestore -Djava.security.manager -Djava.security.policy==config/one.world.policy one.world.Main scripts/startup.one

set PATH=%_PATH_ORIG%

set _PATH_ORIG=
