import React from 'react';
import * as motion from "motion/react-client"

type ButtonProps = {
    children: React.ReactNode;
};

export default function Button({ children }: ButtonProps) {
    return (
       <motion.button
            className="px-4 py-2 rounded-2xl bg-blue-600 text-white hover:bg-blue-700 transition-colors duration-300"
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            >
            {children}
            </motion.button>
            

    );
}