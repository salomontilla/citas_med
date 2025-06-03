export function Button({
    children,
    className = "",
    ...props
    }: React.ButtonHTMLAttributes<HTMLButtonElement>) {
    return (
        <button
        className={`px-4 py-2 rounded-2xl text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 ${className}`}
        {...props}
        >
        {children}
        </button>
    );
    }