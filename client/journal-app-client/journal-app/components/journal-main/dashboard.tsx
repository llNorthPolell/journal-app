import { Journal } from "../../models/journal"

interface DashboardProps{
    journal : Journal
}

export default function Dashboard (props:DashboardProps){



    return (
        <div className="dashboard">
            <div className="dashboard__controls">
                <p>Dashboard Controls</p>
            </div>
            <div className="dashboard__widget-grid">
                <p>Widgets</p>
            </div>

        </div>
    )
}