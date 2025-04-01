import React, { useEffect} from 'react';

const ToggleTema = ({ darkMode, setDarkMode }) => {
    const handleThemeToggle = () => {
        const newTheme = darkMode ? 'light' : 'dark';
        setDarkMode(!darkMode);
        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('theme', newTheme);
    };

    useEffect(() => {
        const savedTheme = localStorage.getItem('theme');
        if (savedTheme) {
            const isDarkMode = savedTheme === 'dark';
            setDarkMode(isDarkMode);
            document.documentElement.setAttribute('data-theme', savedTheme);
        }
    }, [setDarkMode]);

    return (
        <div className="theme-toggler" style={{color: "var(--primario)"}}>
            ><label>
                Modo Oscuro
                <input
                    type="checkbox"
                    checked={darkMode}
                    onChange={handleThemeToggle}
                />

            </label>
        </div>
    );
};

export default ToggleTema;