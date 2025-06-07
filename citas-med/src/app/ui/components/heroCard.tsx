import React from "react";
type HeroCardProps = {
    img?: string,
    title?: string;
    children?: React.ReactNode;
};


export default function HeroCard({img, title, children}: HeroCardProps) {
    return (
        <div className='flex flex-col gap-2 text-gray-50 w-full md:w-60 '>
            <div className='rounded-full bg-gray-50 p-4 w-fit  '>
                <img src={`/mainHero/${img}`} alt="" />
            </div>
            <h1 className='font-bold'>{title}</h1>
            <p>{children}</p>
        </div>)
}