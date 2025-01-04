@template.layout(title="Task Planner - Home",content = @`

<h1 class="text-center my-4">Welcome to the Task Planner</h1>

<p class="text-center">Choose an action:</p>

<ul class="list-group">
    <li class="list-group-item">
        <a href="/tasks">View Tasks</a>
    </li>
    <li class="list-group-item">
        <a href="/tasks/new">Create New Task</a>
    </li>
    <li class="list-group-item">
        <a href="/tags">Manage Tags</a>
    </li>
    <li class="list-group-item">
        <a href="/tasks/completed">View Completed Tasks</a>
    </li>
</ul>

    `)
