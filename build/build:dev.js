import { run } from './utils'
const app = require('../app.json')

// Build service and view
run('npm run buildService:dev')
run('npm run buildView:dev')
// Zip all 
run(`sh -c "zip -r ${app.id}.zip ."`, { cwd: './dist' })