import { Input } from 'baseui/input';
import { Button } from 'baseui/button';
import { useState } from 'react';

const AuthForm = ({ submitLabel, disabled, onSubmit }: any) => {
  const [loginInput, setLoginInput] = useState('');
  const [passwordInput, setPasswordInput] = useState('');

  const handleSubmit = (e: any) => {
    e.preventDefault();
    onSubmit({ username: loginInput, password: passwordInput });
  };

  return (
    <form className='grid gap-2 mt-4' onSubmit={handleSubmit}>
      <Input
        placeholder='Login'
        disabled={disabled}
        value={loginInput}
        onChange={({ target }: any) => setLoginInput(target.value)}
      />
      <Input
        placeholder='Password'
        disabled={disabled}
        type='password'
        value={passwordInput}
        onChange={({ target }: any) => setPasswordInput(target.value)}
      />

      <Button
        isLoading={disabled}
        disabled={disabled}
        type='submit'
      >
        {submitLabel}
      </Button>
    </form>
  );
};

export default AuthForm;
