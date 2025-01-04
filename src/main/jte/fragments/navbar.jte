@import com.example.taskplanner.util.AuthUtil;

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Task Planner</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ml-auto">
            @if(AuthUtil.isAuthenticated())
                <li class="nav-item">
                    <a class="nav-link" href="/tasks">View Tasks</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/tasks/new">Create New Task</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/tags">Manage Tags</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/tasks/completed">View Completed Tasks</a>
                </li>
                <li class="nav-item">
                    <form action="/logout" method="post" style="display:inline;">
                        <button type="submit" class="btn btn-link nav-link">Logout</button>
                    </form>
                </li>
            @else
                <li class="nav-item">
                    <a class="nav-link" href="/login">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/register">Register</a>
                </li>
            @endif
        </ul>
    </div>
</nav>