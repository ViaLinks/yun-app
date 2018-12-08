import { execSync } from 'child_process'

export function run() {
    console.log('<<<<<', arguments[0])
    try {
        const res = execSync(...arguments)
        console.log(res.toString())
        console.log(arguments[0], 'success', '>>>>>')
    } catch (err) {
        console.log(arguments[0], 'failed', '>>>>>')
        throw new Error(err)
    }
}