import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from './App';

test('renders ToDo Liste title', () => {
  render(<App />);
  const titleElement = screen.getByText(/ToDo Liste/i);
  expect(titleElement).toBeInTheDocument();
});

test('renders submit button', () => {
  render(<App />);
  const submitButton = screen.getByText(/Absenden/i);
  expect(submitButton).toBeInTheDocument();
});

test('renders input field', () => {
  render(<App />);
  const inputField = screen.getByRole('textbox');
  expect(inputField).toBeInTheDocument();
});

test('can type in input field', () => {
  render(<App />);
  const inputField = screen.getByRole('textbox');
  fireEvent.change(inputField, { target: { value: 'Test Task' } });
  expect(inputField.value).toBe('Test Task');
});

test('shows loading message', () => {
  render(<App />);
  const loadingMessage = screen.getByText(/Lade ToDos.../i);
  expect(loadingMessage).toBeInTheDocument();
});

test('displays tasks when loaded', async () => {
  global.fetch = jest.fn(() =>
    Promise.resolve({
      ok: true,
      json: () => Promise.resolve([
        { id: 1, taskdescription: 'Test Task 1' },
        { id: 2, taskdescription: 'Test Task 2' }
      ])
    })
  );

  render(<App />);
  
  await waitFor(() => {
    expect(screen.getByText('Test Task 1')).toBeInTheDocument();
    expect(screen.getByText('Test Task 2')).toBeInTheDocument();
  });

  fetch.mockRestore();
});

test('can submit new task', async () => {
  global.fetch = jest.fn()
    .mockResolvedValueOnce({
      ok: true,
      text: () => Promise.resolve('success')
    })
    .mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve([
        { id: 1, taskdescription: 'Neue Task' }
      ])
    });

  render(<App />);
  
  const inputField = screen.getByRole('textbox');
  const submitButton = screen.getByText(/Absenden/i);
  
  fireEvent.change(inputField, { target: { value: 'Neue Task' } });
  fireEvent.click(submitButton);
  
  await waitFor(() => {
    expect(fetch).toHaveBeenCalledWith('http://localhost:8080/tasks', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ taskdescription: 'Neue Task' })
    });
  });

  fetch.mockRestore();
});