import { run } from './utils'
const app = require('../app.json')

// Build App Bundle
run('npm run build:dev')
// Ship to assets directory
run(`cp ./dist/${app.id}.zip ./Android/app/src/main/assets/`)
// Build Apk
run(`chmod +x ./gradlew && ./gradlew assembleDebug`, { cwd: './Android' })