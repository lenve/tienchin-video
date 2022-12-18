// 在这里定义插件
//在插件中，可以引入 vue 组件，并注册（这里的注册，就相当于全局注册）
import MyBanner from "@/components/MyBanner";

export default {
    /**
     * @param app 这个就是 Vue 对象
     * @param options 这是一个可选参数
     *
     * 当项目启动的时候，插件方法就会自动执行
     */
    install: (app, options) => {
        console.log("这是我的第一个插件")
        //在这里完成组件的注册，注意，这是一个全局注册
        app.component('my-banner', MyBanner);
        //自定义指令，第一个参数是自定义指令的名称，第二个参数自定义指令的逻辑
        //el 表示添加这个自定义指令的节点
        //binding 中包含了自定义指令的参数
        app.directive('font-size', (el, binding, vnode) => {
            let size = 18;
            //binding.arg 获取到的就是 small 或者 large
            switch (binding.arg) {
                case "small":
                    size = options.fontSize.small;
                    break;
                case "large":
                    size = options.fontSize.large;
                    break;
                default:
                    break;
            }
            //为使用了 v-font-size 指令的标签设置 font-size 的大小
            el.style.fontSize = size + 'px';
        })

        const clickMe = () => {
            console.log("========clickMe========")
        }
        //这里相当于是注册方法
        app.provide('clickMe', clickMe);
    }
}
