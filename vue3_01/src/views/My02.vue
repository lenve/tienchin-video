<template>
    <div>
        <div>hello 01!</div>
        <h1>{{msg}}</h1>
        <input type="text" v-model="msg">
        <button @click="doLogin('zhangsan','123')">登录</button>
        <div>{{currentTime}}</div>
    </div>
</template>

<script>

    import {ref} from 'vue';
    //使用钩子函数时，首先导入钩子函数
    //计算属性的使用，也需要首先导入计算属性
    import {onMounted,computed,watch} from 'vue';

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
            let age = ref(99);
            const doLogin=(username,password)=>{
                console.log(username);
                console.log(password);
                age.value++;
                msg.value = 'hello javaboy!';
            }
            //调用钩子函数，并传入回调函数
            //另外需要注意，这个钩子函数不需要返回
            onMounted(()=>{
                console.log("My02 初始化了。。。")
            })
            //现在就可以通过计算属性去定义一个变量了
            const currentTime=computed(()=>{
                age.value++;
                return Date.now();
            })
            watch(age,(newValue,oldValue)=>{
                console.log("newValue", newValue);
                console.log("oldValue", oldValue);
            })
            //注意，计算属性需要在 return 中返回
            return {msg,doLogin,currentTime,age};
        }
    }
</script>

<style scoped>

</style>
