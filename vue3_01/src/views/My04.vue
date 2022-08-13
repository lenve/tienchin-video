<template>
    <div>
        <div>{{age}}</div>
        <div>{{book.name}}</div>
        <div>{{book.author}}</div>
        <div>{{name}}</div>
        <div>{{author}}</div>
        <button @click="updateBookInfo">更新图书信息</button>
        <button @click="btnClick">ClickMe</button>
        <button @click="go">Go My03</button>
        <button @click="btnClick2">StoreDemo</button>
    </div>
</template>

<!--直接在 script 节点中定义 setup 属性，然后，script 节点就像以前 jquery 写法一样-->
<script setup>

    //getCurrentInstance 方法可以获取到当前的 Vue 对象
    import {ref, reactive, toRefs, onMounted, getCurrentInstance} from 'vue';

    //来自该方法的 proxy 对象则相当于之前的 this
    const {proxy} = getCurrentInstance();

    const age = ref(99);
    const book = reactive({
        name: "三国演义",
        author: '罗贯中'
    });
    const updateBookInfo = () => {
        //修改书名，注意，在 vue3 中，现在方法中访问变量，不再需要 this
        book.name = '三国演义123';
    }
    //展开的变量
    const {name, author} = toRefs(book);
    onMounted(() => {
        console.log(this);
    })
    const btnClick = () => {
        //想在这里调用全局方法
        proxy.sayHello();
    }
    function go() {
        proxy.$router.push('/my03');

    }
    function btnClick2() {
        console.log(proxy.$store.state.name)
        proxy.$store.commit('SET_NAME', '江南一点雨');
        console.log(proxy.$store.state.name)

    }
</script>

<style scoped>

</style>
