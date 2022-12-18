import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 导入插件
import plugins from './plugins'


const app = createApp(App);
/**
 * 这里定义的，就是一个全局指令
 */
app.directive('onceClick2', {
    /**
     * 首先，自定义指令的钩子函数一共有七个：https://cn.vuejs.org/guide/reusability/custom-directives.html#directive-hooks
     * 钩子函数中有参数，这个参数基本和 Vue2 中的自定义指令时候的参数含义一致，一共四个参数
     * @param el 就是自定义指令所绑定的元素
     * @param binding 各种传递的参数都在 binding 上
     * @param vNode Vue 编译生成的虚拟节点
     */
    mounted(el, binding, vNode) {
        el.addEventListener('click', () => {
            if (!el.disabled) {
                //当发生点击事件的时候，如果 el 没有被禁用，那么就让他禁用 10s 钟
                //禁用
                el.disabled = true;
                //延迟执行
                setTimeout(() => {
                    el.disabled = false;
                }, binding.value || 3000);
            }
        });
    }
});
/**
 * 这里定义的，就是一个全局指令
 */
app.directive('onceClick3', {
    /**
     * 首先，自定义指令的钩子函数一共有七个：https://cn.vuejs.org/guide/reusability/custom-directives.html#directive-hooks
     * 钩子函数中有参数，这个参数基本和 Vue2 中的自定义指令时候的参数含义一致，一共四个参数
     * @param el 就是自定义指令所绑定的元素
     * @param binding 各种传递的参数都在 binding 上
     * @param vNode Vue 编译生成的虚拟节点
     */
    mounted(el, binding, vNode) {
        el.addEventListener('click', () => {
            if (!el.disabled) {
                //当发生点击事件的时候，如果 el 没有被禁用，那么就让他禁用 10s 钟
                //禁用
                el.disabled = true;
                let time = binding.value;
                if (binding.arg == 's') {
                    //使用指令的时候，单位是秒，这里转为 ms，因为 setTimeout 中的时间是 ms
                    time = time * 1000;
                }
                //延迟执行
                setTimeout(() => {
                    el.disabled = false;
                }, time);
            }
        });
    }
});

// 这个表示当前用户所具备的权限信息，正常来说，这个数组应该是从服务端获取
const usersPermissions = ['user'];

app.directive('hasPermission', {
    mounted(el, binding, vNode) {
        //获取组件所需要的权限 <button v-hasPermission="[user:add]">添加用户</button>
        //此时，拿到的 value 就是一个数组：[user:add]
        const {value} = binding;  //const value = binding.value;
        //接下来就是判断 usersPermissions 数组中是否包含 value 数组中的值
        let f=usersPermissions.some(p=>{
            //如果这里都返回 false，f 就是 false，如果这里有一个是 true，则 f 就是 true
            return p.indexOf(value[0]) !== -1;
        })
        if (!f) {
            //说明当前用户不具备所需要的权限，那么就把 el 从 DOM 树中移除掉
            el.parentNode && el.parentNode.removeChild(el);
        }

    }
})

app
    //安装插件
    .use(plugins, {
        fontSize: {
            small: 6,
            large: 64
        }
    })
    .use(store)
    .use(router)
    .mount('#app')
