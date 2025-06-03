import React from 'react';

type ButtonProps = {
    children: React.ReactNode;
};

export default function Button({ children }: ButtonProps) {
    return (
        <button className='mt-6 w-80  px-6 py-3 rounded-2xl bg-blue-600 hover:bg-blue-700 text-white transition-colors duration-300 text-center cursor-pointer'>
            {children}
        </button>
    );
}