<template>
    <div>
        <h1>My03</h1>
        <div>{{age}}</div>
        <div>{{name}}</div>
        <div>{{author}}</div>
        <button @click="updateBookInfo">更新图书信息</button>
    </div>
</template>

<script>

    import {ref, reactive,toRefs} from 'vue';

    export default {
        name: "My03",
        setup() {
            const age = ref(99);
            const book = reactive({
                name: "三国演义",
                author: '罗贯中'
            });
            const updateBookInfo = ()=>{
                //修改书名，注意，在 vue3 中，现在方法中访问变量，不再需要 this
                book.name = '三国演义123';
            }
            //如果直接这样写，会导致响应式失效，通过 toRefs 函数，可以解决这个问题
            return {age,...toRefs(book),updateBookInfo};
        }
    }
</script>

<style scoped>

</style>
