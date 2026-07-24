#  Need to change path so that dynamically linked native libraries can
#  be loaded.
export GUID_STATE=config
export LD_LIBRARY_PATH="./bin:$LD_LIBRARY_PATH"
java -cp bin/one.world.jar:bin/one.tools.jar:bin/one.apps.jar:bin/db.jar:bin/BCEL.jar -Done.world.config.name=./config/one.world.config -Done.world.home=. -Done.world.store.root=./tuplestore -Djava.security.manager -Djava.security.policy==config/one.world.policy one.world.Main scripts/startup.one
