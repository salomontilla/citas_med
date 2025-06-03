import React from 'react';
type CardProps = {title?: string, icon?: string, number?: string, children?: React.ReactNode};

export default function CtaCard({children,title, icon, number}: CardProps) {
    return(
        <div>
            <div className="bg-white text-blue-950 p-6 rounded-lg shadow-lg flex flex-col items-start gap-4 w-[20%] h-auto">
                <div className='flex items-start gap-4'>
                    <div className='rounded-full bg-blue-600 text-white w-8 h-8 flex items-center justify-center'>
                        <p>{number}</p>
                    </div>
                    <h2 className="text-xl font-bold">{title}</h2>
                </div>
                <p className="opacity-80">{children}</p>
                <img src={`${icon}`} alt="" className='aspect-square object-contain h-auto w-9'></img>
                
            </div>
        </div>
    )
}