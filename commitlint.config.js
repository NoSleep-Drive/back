module.exports = {
    rules: {
        'type-enum': [
            2,
            'always',
            ['feat', 'fix', 'chore', 'docs', 'refactor', 'test', 'style', 'ci', 'perf'],
        ],
        'type-case': [2, 'always', 'lower-case'],
        'type-empty': [2, 'never'],
        'subject-empty': [2, 'never'],
        'subject-case': [0], // 자유롭게
        'header-pattern': [/^(\w+)\[#\d+\]:\s.+$/],
        'header-pattern-required': [2, 'always'],
    },
};