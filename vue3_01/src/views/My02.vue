<template>
    <div>
        <div>hello 01!</div>
        <h1>{{msg}}</h1>
        <input type="text" v-model="msg">
    </div>
</template>

<script>

    import {ref} from 'vue';

    export default {
        name: "My02",
        /**
         * 我们以前在 Vue2 中定义的各种变量、方法、生命周期钩子函数等等，现在统一都在 setup 中进行定义
         *
         * 需要注意的是，所有定义的变量，方法等，都需要返回之后才可以使用
         */
        setup() {
            //注意，直接这样写，这个变量不是响应式数据
            // let msg = "hello vue3";
            let msg = ref("hello vue3");
            return {msg};
        }
    }
</script>

<style scoped>

</style>
