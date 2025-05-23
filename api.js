import axios from 'axios';

const API_BASE = "http://localhost:8080";

export const getTodos = () => axios.get(`${API_BASE}/todos`);
export const addTodo = (todo) => axios.post(`${API_BASE}/todos`, todo);
export const deleteTodo = (id) => axios.delete(`${API_BASE}/todos/${id}`);
export const summarizeTodos = () => axios.post(`${API_BASE}/summarize`);
