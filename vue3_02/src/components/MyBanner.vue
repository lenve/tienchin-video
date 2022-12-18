<template>
    <div>
        <div>
            <a href="http://www.javaboy.org" v-font-size:large>javaboy</a>
        </div>
        <div>
            <!--使用自定义指令，去指定文本的大小，指令的名字就是 font-size-->
            <a href="http://www.itboyhub.com" v-font-size:small>itboyhub</a>
        </div>
        <div>
            <button @click="btnClick" v-onceClick2="10000" v-hasPermission="['user']">ClickMe</button>
        </div>
    </div>
</template>

<script>
    import {inject} from 'vue';

    export default {
        name: "MyBanner",
        mounted() {
            //注入 clickMe 函数
            const clickMe = inject('clickMe');
            clickMe();
        },
        methods: {
            btnClick() {
            }
        }
    }
</script>

<style scoped>

</style>
