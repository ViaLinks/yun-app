import { run } from './utils'
const app = require('../app.json')

// Build App Bundle
run('npm run build:dev')
// Ship to assets directory
run(`sh -c "cp ./dist/${app.id}.zip ./Android/app/src/main/assets/"`)
// Build Apk
run(`sh -c "chmod +x ./gradlew && ./gradlew assembleDebug"`, { cwd: './Android' })