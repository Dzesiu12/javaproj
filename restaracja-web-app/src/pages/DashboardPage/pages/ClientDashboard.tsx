import { useAuth } from '../../../providers/AuthProvider';
import { Button } from 'baseui/button';
import { Card } from 'baseui/card';
import { AnimateSharedLayout, motion } from 'framer-motion';
import { useEffect, useState } from 'react';
import { BsFillCartPlusFill } from 'react-icons/bs';
import { MdDone, MdRemoveShoppingCart } from 'react-icons/md';
import { toast } from 'react-toastify';
import { useInterval } from 'react-use';
import { Tag } from 'baseui/tag';

const ClientDashboard = () => {
  const { authToken } = useAuth();

  const [dishes, setDishes] = useState<any[]>([]);
  const [orderDishes, setOrderDishes] = useState<any[]>([]);
  const [orders, setOrders] = useState<any[]>([]);

  const fetchDishes = async () => {
    const res = await fetch('http://localhost:8080/api/dishes', {
      headers: { Authorization: `Bearer ${authToken}` },
    });

    const { data } = await res.json();
    setDishes(data);
  };

  const fetchOrders = async () => {
    const res = await fetch('http://localhost:8080/api/orders/users', {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    });

    const { data } = await res.json();
    setOrders(data.orders);
    console.log(data.orders);
  };

  useEffect(() => {
    fetchDishes();
    fetchOrders();
  }, []);

  useInterval(() => {
    fetchDishes();
    fetchOrders();
  }, 5000);

  const handleAddToOrder = (dish: any) => {
    const existing = orderDishes.find(d => d.id === dish.id);

    if (existing) {
      setOrderDishes(orderDishes.map((d: any) => d.id === dish.id ? ({ ...d, qty: d.qty + 1 }) : d));
    } else {
      setOrderDishes([...orderDishes, {
        id: dish.id,
        qty: 1,
        name: dish.name,
      }]);
    }
  };

  const handleRemoveFromOrder = (dishId: string) => {
    const existing = orderDishes.find(d => d.id === dishId);
    if (existing.qty > 1) {
      setOrderDishes(orderDishes.map((d: any) => d.id === dishId ? ({ ...d, qty: d.qty - 1 }) : d));
    } else {
      setOrderDishes(orderDishes.filter(d => d.id !== dishId));
    }
  };

  const handleDeleteFromOrder = (dishId: string) => {
    setOrderDishes(orderDishes.filter(d => d.id !== dishId));
  };

  const handlePlaceOrder = async () => {
    if (orderDishes.length > 0) {
      const res = await fetch('http://localhost:8080/api/orders/users', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${authToken}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          dishes: orderDishes.map(d => ({
            dishId: d.id,
            quantity: d.qty,
          })),
        }),
      });

      const { responseType, message } = await res.json();
      if (responseType === 'SUCCESS') {
        fetchOrders();
        setOrderDishes([]);
        toast.success(message);
      }
    }
  };

  return (
    <div className='grid grid-cols-3 gap-6'>
      <div>
        <h2 className='text-2xl font-bold mb-4'>Available dishes</h2>

        <AnimateSharedLayout>
          <ul className='flex flex-col space-y-2'>
            {dishes.map((dish: any) => (
              <motion.li
                layout
                key={dish.id}
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
              >
                <Card>
                  <div className='flex items-center justify-between'>
                    <p className='mr-6'>{dish.name}</p>
                    <Button
                      type='button'
                      size='compact'
                      kind='secondary'
                      shape='circle'
                      onClick={() => handleAddToOrder(dish)}
                    >
                      <BsFillCartPlusFill />
                    </Button>
                  </div>
                </Card>
              </motion.li>
            ))}
          </ul>
        </AnimateSharedLayout>
      </div>

      <div>
        <h2 className='text-2xl font-bold mb-4'>Your order</h2>
        {orderDishes.length === 0 && <p>Chose dishes from left</p>}

        <AnimateSharedLayout>
          <ul className='flex flex-col space-y-2'>
            {orderDishes.map((dish: any) => (
              <motion.li
                layout
                key={dish.id}
                className='flex flex-col'
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
              >
                <Card>
                  <div className='flex items-center'>
                    <p className='mr-2'>{dish.qty}x</p>
                    <p className='mr-6 font-medium'>{dish.name}</p>
                    <div className='ml-auto flex space-x-2'>
                      {dish.qty > 1 && (
                        <Button
                          type='button'
                          size='compact'
                          kind='secondary'
                          shape='circle'
                          onClick={() => handleRemoveFromOrder(dish.id)}
                        >
                          -1
                        </Button>
                      )}

                      <Button
                        type='button'
                        size='compact'
                        kind='secondary'
                        shape='circle'
                        onClick={() => handleDeleteFromOrder(dish.id)}
                      >
                        <MdRemoveShoppingCart />
                      </Button>
                    </div>
                  </div>

                </Card>

              </motion.li>
            ))}
          </ul>

          {orderDishes.length > 0 && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              layout
              className='mt-4 flex flex-col rounded overflow-hidden'
            >
              <Button type='button' size='large' onClick={handlePlaceOrder}>
                Place order
              </Button>
            </motion.div>
          )}
        </AnimateSharedLayout>
      </div>

      <div>
        <h2 className='text-2xl font-bold mb-4'>Your orders</h2>
        {orders.length === 0 && <p>Make an order</p>}

        <ul className='mt-6 flex flex-col space-y-2'>
          {orders.sort((a, b) => a.isCompleted - b.isCompleted).map((order: any) => (
            <motion.li
              layout
              key={order.id}
              className='flex flex-col'
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
            >
              <Card>
                <div className='flex'>
                  <div>
                    {order.isCompleted ? (
                      <Tag kind='green' closeable={false}>Done</Tag>
                    ) : (
                      <Tag kind='yellow' closeable={false}>Waiting</Tag>
                    )}

                    <div className='mt-2'>
                      <ul className='flex flex-col space-y-2'>
                        {order.dishes.map(({ id, name, quantity }: any) => (
                          <li key={id}>{quantity}x <span className="font-bold">{name}</span></li>
                        ))}
                      </ul>
                    </div>
                  </div>
                </div>
                  <div>
                 {order.price / 100 } zł
               </div>
              </Card>
            </motion.li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default ClientDashboard;
