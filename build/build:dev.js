import { run } from './utils'
const app = require('../app.json')

// Build service and view
run('npm run buildService:dev')
run('npm run buildPages:dev')
// Copy 'app.json'
run(`cp ./app.json ./dist/`)
// Zip all 
run(`zip -r ${app.id}.zip .`, { cwd: './dist' })