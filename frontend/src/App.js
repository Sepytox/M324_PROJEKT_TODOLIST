import logo from './logo.svg';
import './App.css';
import React from 'react';

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      todos: typeof props.todos === 'undefined' ? [] : props.todos,
      taskdescription: "",
      editingId: null,
      editText: ""
    };
  }

  handleChange = event => {
    this.setState({ taskdescription: event.target.value });
  }

  handleSubmit = event => {
    event.preventDefault();
    console.log("Sending task description to Spring-Server: "+this.state.taskdescription);
    fetch("http://localhost:8080/tasks", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ taskdescription: this.state.taskdescription })
    })
    .then(response => {
      console.log("Receiving answer after sending to Spring-Server: ");
      console.log(response);
      this.componentDidMount();
      this.setState({taskdescription: ""});
    })
    .catch(error => console.log(error))
  }

  componentDidMount() {
    fetch("http://localhost:8080")
      .then(response => response.json())
      .then(data => {
        console.log("Receiving task list data from Spring-Server: ");
        console.log(data);
        this.setState({todos: data});
      })
      .catch(error => console.log(error))
  }

  handleClick = taskdescription => {
    console.log("Sending task description to delete on Spring-Server: "+taskdescription);
    fetch(`http://localhost:8080/delete`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ taskdescription: taskdescription })
    })
    .then(response => {
      console.log("Receiving answer after deleting on Spring-Server: ");
      console.log(response);
      window.location.href = "/";
    })
    .catch(error => console.log(error))
  }

  handleEditClick = (todo) => {
    this.setState({
      editingId: todo.id,
      editText: todo.taskdescription
    });
  }

  handleCancelEdit = () => {
    this.setState({
      editingId: null,
      editText: ""
    });
  }

  handleEditChange = (event) => {
    this.setState({ editText: event.target.value });
  }

  handleSaveEdit = (todoId) => {
    console.log("Sending edit request to Spring-Server for ID: " + todoId);
    fetch(`http://localhost:8080/edit`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ 
        id: todoId, 
        newDescription: this.state.editText 
      })
    })
    .then(response => {
      console.log("Receiving answer after editing on Spring-Server: ");
      console.log(response);
      this.setState({
        editingId: null,
        editText: ""
      });
      this.componentDidMount();
    })
    .catch(error => console.log(error))
  }

  renderTasks(todos) {
    return (
      <ul>
        {todos.map((todo, index) => (
          <li key={todo.id || todo.taskdescription}>
            {this.state.editingId === todo.id ? (
              <div>
                <span>Task {index + 1}: </span>
                <input 
                  type="text" 
                  value={this.state.editText} 
                  onChange={this.handleEditChange}
                  style={{marginLeft: '5px', marginRight: '5px'}}
                />
                <button onClick={() => this.handleSaveEdit(todo.id)} style={{marginRight: '5px'}}>Save</button>
                <button onClick={this.handleCancelEdit}>Cancel</button>
              </div>
            ) : (
              <div>
                <span>Task {index + 1}: {todo.taskdescription}</span>
                <button onClick={() => this.handleEditClick(todo)} style={{marginLeft: '10px', marginRight: '5px'}}>Edit</button>
                <button onClick={this.handleClick.bind(this, todo.taskdescription)}>Done</button>
              </div>
            )}
          </li>
        ))}
      </ul>
    );
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1>
            ToDo Liste
          </h1>
          <form onSubmit={this.handleSubmit}>
            <input
              type="text"
              value={this.state.taskdescription}
              onChange={this.handleChange}
            />
            <button type="submit">Absenden</button>
          </form>
          <div>
            {this.renderTasks(this.state.todos)}
          </div>
        </header>
      </div>
    );
  }

}

export default App;
