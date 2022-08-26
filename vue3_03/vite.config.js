import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import VueSetupExtend from 'vite-plugin-vue-setup-extend'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue(), AutoImport({
        imports: ['vue', 'vue-router']
    }),
        VueSetupExtend()],
    resolve: {
        extensions: ['.vue', '.js', '.json', '.ts']
    }
})
