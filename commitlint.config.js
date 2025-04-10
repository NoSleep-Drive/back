module.exports = {
    parserPreset: {
        parserOpts: {
            headerPattern: /^(\w+)\[#(\d+)\]:\s(.+)$/,
            headerCorrespondence: ['type', 'ticket', 'subject'],
        },
    },
    rules: {
        'header-match-pattern': [2, 'always', /^(\w+)\[#(\d+)\]:\s(.+)$/],
        'type-enum': [
            2,
            'always',
            ['feat', 'fix', 'chore', 'docs', 'refactor', 'test', 'style', 'ci', 'perf'],
        ],
    },
};
