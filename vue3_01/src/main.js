import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

let app = createApp(App);

/**
 * Vue3 中定义全局方法
 */
app.config.globalProperties.sayHello=()=>{
    console.log("hello javaboy!");
}

app.use(store).use(router).mount('#app')
