import React, { useEffect, useState } from 'react';
import { getTodos, addTodo, deleteTodo, summarizeTodos } from './api';

function App() {
  const [todos, setTodos] = useState([]);
  const [text, setText] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    const res = await getTodos();
    setTodos(res.data);
  };

  const handleAdd = async () => {
    await addTodo({ title: text });
    setText('');
    fetchTodos();
  };

  const handleDelete = async (id) => {
    await deleteTodo(id);
    fetchTodos();
  };

  const handleSummarize = async () => {
    try {
      const res = await summarizeTodos();
      setMessage(res.data);
    } catch (err) {
      setMessage('Failed to send to Slack');
    }
  };

  return (
    <div style={{ padding: 20 }}>
      <h2>Todo Summary Assistant</h2>
      <input value={text} onChange={(e) => setText(e.target.value)} />
      <button onClick={handleAdd}>Add</button>
      <ul>
        {todos.map(todo => (
          <li key={todo.id}>
            {todo.title} <button onClick={() => handleDelete(todo.id)}>❌</button>
          </li>
        ))}
      </ul>
      <button onClick={handleSummarize}>Summarize & Send to Slack</button>
      {message && <p>{message}</p>}
    </div>
  );
}

export default App;

