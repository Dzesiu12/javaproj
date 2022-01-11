import { Input } from 'baseui/input';
import { Button } from 'baseui/button';
import { RiAddFill } from 'react-icons/ri';
import { Card } from 'baseui/card';
import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { useAuth } from '../../../providers/AuthProvider';
import { RiDeleteBin2Line } from 'react-icons/ri';
import { AnimateSharedLayout, motion } from 'framer-motion';

const ManagerDashboard = () => {
  const { authToken } = useAuth();

  const [dishName, setDishName] = useState<string>('');
  const [dishes, setDishes] = useState<any[]>([]);

  useEffect(() => {
    (async () => {
      const res = await fetch('http://localhost:8080/api/dishes', {
        headers: { Authorization: `Bearer ${authToken}` },
      });

      const { data } = await res.json();
      setDishes(data);
    })();
  }, []);

  const handleAddDish = async (e: any) => {
    e.preventDefault();
    const res = await fetch('http://localhost:8080/api/dishes', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${authToken}`,
      },
      body: JSON.stringify({ name: dishName }),
    });

    const { responseType, data, message } = await res.json();
    if (responseType === 'SUCCESS') {
      toast.success(message);
      setDishes([...dishes, data]);
      setDishName('');
    } else {
      toast.error(message);
    }
  };

  const handleDeleteDish = async (dishId: string) => {
    const res = await fetch(`http://localhost:8080/api/dishes/${dishId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${authToken}`,
      },
    });

    const { responseType, message } = await res.json();
    if (responseType === 'SUCCESS') {
      toast.success(message);
      setDishes(dishes.filter((dish: any) => dish.id !== dishId));
    } else {
      toast.error(message);
    }
  };

  return (
    <div>
      <Card>
        <form className='flex space-x-4' onSubmit={handleAddDish}>
          <Input
            placeholder='Dish name'
            value={dishName}
            onChange={({ target }: any) => setDishName(target.value)}
          />
          <Button
            type='submit'
            endEnhancer={() => <RiAddFill />}
          >
            Add
          </Button>
        </form>
      </Card>

      <div className='mt-8'>
        <AnimateSharedLayout>
          <ul className='flex flex-col space-y-2'>
            {dishes.map((dish: any) => (
              <motion.li
                layout
                key={dish.id}
                className='flex flex-col'
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
              >
                <Card>
                  <div className='flex items-center justify-between'>
                    <div className='mr-6'>{dish.name}</div>
                    <Button
                      type='button'
                      size='compact'
                      kind='secondary'
                      shape='circle'
                      onClick={() => handleDeleteDish(dish.id)}
                    >
                      <RiDeleteBin2Line />
                    </Button>
                  </div>
                </Card>
              </motion.li>
            ))}
          </ul>
        </AnimateSharedLayout>
      </div>
    </div>
  );
};

export default ManagerDashboard;
