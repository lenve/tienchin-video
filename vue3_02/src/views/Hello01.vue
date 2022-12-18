<template>
    <div>
        <div>{{a}}</div>
        <div>
            <button @click="btnClick" v-onceClick2="10000">ClickMe</button>
            <hr>
            <button @click="btnClick" v-onceClick3:[timeUnit]="3">ClickMe</button>
        </div>
    </div>
</template>

<script>
    import {ref} from 'vue';

    export default {
        setup() {
            const a = ref(1);
            const timeUnit = ref('s');
            const btnClick = () => {
                a.value++;
            }
            return {a, btnClick, timeUnit}
        },
        directives:{
            onceClick:{
                /**
                 * 首先，自定义指令的钩子函数一共有七个：https://cn.vuejs.org/guide/reusability/custom-directives.html#directive-hooks
                 * 钩子函数中有参数，这个参数基本和 Vue2 中的自定义指令时候的参数含义一致，一共四个参数
                 * @param el 就是自定义指令所绑定的元素
                 * @param binding 各种传递的参数都在 binding 上
                 * @param vNode Vue 编译生成的虚拟节点
                 */
                mounted(el, binding, vNode) {
                    el.addEventListener('click',()=>{
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
            }
        }
    }
</script>

<style scoped>

</style>
