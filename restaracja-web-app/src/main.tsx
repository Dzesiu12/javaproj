import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import AuthProvider from './providers/AuthProvider';
import { Client as Styletron } from 'styletron-engine-atomic';
import { Provider as StyletronProvider } from 'styletron-react';
import { DarkTheme, BaseProvider } from 'baseui';
import './index.css';
import 'react-toastify/dist/ReactToastify.min.css';

const engine = new Styletron();

ReactDOM.render(
  <React.StrictMode>
    <AuthProvider>
      <StyletronProvider value={engine}>
        <BaseProvider theme={DarkTheme}>
          <App />
        </BaseProvider>
      </StyletronProvider>
    </AuthProvider>
  </React.StrictMode>,
  document.getElementById('root'),
);
