<template>
  <div class="login-form">
    <h2>正版登录页面</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="username">账号:</label>
        <input type="text" id="username" v-model="username" required />
      </div>
      <div class="form-group">
        <label for="password">密码:</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit">登录</button>
    </form>
    <p v-if="message" :class="{ success: isSuccess, error: !isSuccess }">
      {{ message }}
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const username = ref('admin'); // 预填方便测试
const password = ref('password123'); // 预填方便测试
const message = ref('');
const isSuccess = ref(false);

const handleSubmit = async () => {
  try {
    // 请求正版后端的 API
    const response = await axios.post('http://localhost:9090/api/login', {
      username: username.value,
      password: password.value,
    }, { withCredentials: true }); // withCredentials 用于发送和接收 cookie

    message.value = response.data.message;
    isSuccess.value = response.data.success;
  } catch (error) {
    if (error.response) {
      message.value = error.response.data.message;
    } else {
      message.value = '网络请求失败';
    }
    isSuccess.value = false;
  }
};
</script>

<style scoped>
.login-form {
  width: 300px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
.form-group {
  margin-bottom: 15px;
}
label {
  display: block;
  margin-bottom: 5px;
}
input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}
button {
  width: 100%;
  padding: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.success {
  color: green;
}
.error {
  color: red;
}
</style>