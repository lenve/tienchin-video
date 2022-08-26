<script setup name="javaboy-app">
    // This starter template is using Vue 3 <script setup> SFCs
    // Check out https://vuejs.org/api/sfc-script-setup.html#script-setup
    import HelloWorld from './components/HelloWorld.vue'
    // import {getCurrentInstance} from 'vue';

    const {proxy} = getCurrentInstance()

    function goUserMana() {
        proxy.$router.push('/um');
    }

    // import {useRouter} from 'vue-router';

    const router = useRouter();

    function goLogMana() {
        router.push("/lm");
    }
</script>

<!--<script>-->
<!--    export default {-->
<!--        name: 'my-app'-->
<!--    }-->
<!--</script>-->

<template>
    <div>
        <a href="https://vitejs.dev" target="_blank">
            <img src="/vite.svg" class="logo" alt="Vite logo"/>
        </a>
        <a href="https://vuejs.org/" target="_blank">
            <img src="./assets/vue.svg" class="logo vue" alt="Vue logo"/>
        </a>
    </div>
    <button @click="goUserMana">用户管理</button>
    <button @click="goLogMana">日志管理</button>
    <router-view></router-view>
    <!--  <HelloWorld msg="Vite + Vue" />-->
</template>

<style scoped>
    .logo {
        height: 6em;
        padding: 1.5em;
        will-change: filter;
    }

    .logo:hover {
        filter: drop-shadow(0 0 2em #646cffaa);
    }

    .logo.vue:hover {
        filter: drop-shadow(0 0 2em #42b883aa);
    }
</style>
