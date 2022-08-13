<template>
    <div>hello 01!</div>
    <h1>{{msg}}</h1>
</template>

<script>
    export default {
        name: "My01",
        data(){
            return{
                msg: "hello javaboy!"
            }
        },
        mounted() {
            console.log("=====Vue2=====mounted()==========")
        }
    }
</script>

<style scoped>

</style>
