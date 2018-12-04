echo "We are now building Android apk...\n"
npm install 
npm run build:dev 
rm -f ./Android/lib/src/main/assets/apps/*.zip 
cp ./dist/*.zip ./Android/lib/src/main/assets/apps/
cd ./Android/
gradlew assembleDebug
adb install -r ./app/build/outputs/apk/debug/app-debug.apk

adb shell am start com.walfud.android/com.yunapp.android.MainActivity