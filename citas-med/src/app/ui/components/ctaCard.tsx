import React from 'react';

type CardProps = { 
    title?: string, 
    icon?: string, 
    number?: string, 
    children?: React.ReactNode 
};

export default function CtaCard({ children, title, icon, number }: CardProps) {
    return (
        <div>
            <div className="card bg-white text-blue-950 p-6 rounded-4xl shadow-lg flex flex-col items-start gap-4 w-full h-80">
                <div className='relative flex items-start gap-4'>
                    <div className='rounded-full bg-blue-600 text-white w-[32px] h-[32px] flex items-center justify-center'>
                        <p>{number}</p>
                    </div>
                    <h2 className="text-xl font-bold">{title}</h2>
                </div>
                <img src={`/cta/${icon}`} alt="logo" className='aspect-square object-contain h-auto w-16'></img>
                <p className="opacity-80 text-lg">{children}</p>

            </div>
            <style>
                {`
                @keyframes show {
                    from {
                        opacity: 0; scale: 10%;
                    }
                    to {
                        opacity: 1; scale: 100%;
                    }
                }

                .card, h2 {
                    view-timeline-name: --reveal;

                    animation-name: show;
                    animation-fill-mode: both;

                    animation-timeline: --reveal;
                    animation-range: entry 25% cover 50%;
                }
                `}
            </style>
        </div>
    )
}