const stripe = Stripe('pk_test_51PXSWpAPR6ydRKpZhipbfehgjTVD5H6sRy0BiymRxaTYzp5EwPsSa4ZyKculUTkLAIou21kRLY9zf9MkhywRTyOB00uNKqPHXX');
const elements = stripe.elements();

const cardElement = elements.create('card');
cardElement.mount('#card-element');

const form = document.getElementById('payment-form');
form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const {paymentMethod, error} = await stripe.createPaymentMethod({
        type: 'card',
        card: {
            number: '4242424242424242',
            exp_month: 12,
            exp_year: 2024,
            cvc: '123'
        }
    });

    if (error) {
        console.error(error);
    } else {
        const paymentMethodId = paymentMethod.id;
        console.log('PaymentMethodId:', paymentMethodId);

        try {
            const response = await fetch('http://localhost:8080/api/v1/payment/create-payment-intent', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    paymentMethodId: paymentMethodId,
                    amount: 1000,
                    currency: 'usd'
                }),
            });

            const result = await response.json();
            console.log('Payment Intent created:', result);

        } catch (error) {
            console.error('Error:', error);
        }
    }
});