import React from 'react';

import Navbar from '../components/navbar';
import Footer from '../components/footer';

interface LayoutProps {
    children: React.ReactNode
}

function Layout ({children} : LayoutProps){
    return (
        <>
            <Navbar/>
            {children}
            <Footer/>
        </>
    );
}

export default Layout;