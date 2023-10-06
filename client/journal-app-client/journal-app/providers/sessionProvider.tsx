"use client"

import { Session } from "next-auth";
import { SessionProvider } from "next-auth/react";
import { ReactNode } from "react";

interface ClientSessionProviderProps{
    session: Session,
    children: ReactNode
}

export default function ClientSessionProvider({session,children} : ClientSessionProviderProps){
    return (
        <SessionProvider session={session}>
            {children}
        </SessionProvider>
    )
}